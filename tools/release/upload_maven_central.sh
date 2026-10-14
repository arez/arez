#!/usr/bin/env bash
set -euo pipefail

usage() {
  echo "Usage: tools/release/upload_maven_central.sh <version>" >&2
}

validate_version() {
  local version="$1"
  if [[ ! "${version}" =~ ^[0-9]+(\.[0-9]+){1,2}$ ]]; then
    echo "Release version must be non-empty and must not end with -SNAPSHOT: ${version}" >&2
    exit 1
  fi
}

extract_deployment_state() {
  local file="$1"
  python3 - "${file}" <<'PY'
import json
import sys
with open(sys.argv[1]) as source:
    print(json.load(source).get('deploymentState', ''))
PY
}

print_status_failure() {
  local message="$1"
  local deployment_id="$2"
  local last_status="$3"
  local response_file="$4"

  echo "${message}" >&2
  echo "Deployment id: ${deployment_id}" >&2
  echo "Last status: ${last_status}" >&2
  if [[ -s "${response_file}" ]]; then
    echo "Response body:" >&2
    cat "${response_file}" >&2
  fi
}

require_tag_ready() {
  local version="$1"
  local default_branch
  local current_branch
  local head_tag
  local tag="v${version}"

  git remote get-url origin >/dev/null

  default_branch="$(git symbolic-ref --quiet --short refs/remotes/origin/HEAD 2>/dev/null || true)"
  default_branch="${default_branch#origin/}"
  if [[ -z "${default_branch}" ]]; then
    default_branch="$(gh repo view --json defaultBranchRef --jq .defaultBranchRef.name)"
  fi
  if [[ -z "${default_branch}" ]]; then
    echo "Unable to determine origin default branch." >&2
    exit 1
  fi

  git fetch origin "+refs/heads/${default_branch}:refs/remotes/origin/${default_branch}" --tags

  current_branch="$(git symbolic-ref --quiet --short HEAD || true)"
  if [[ -z "${current_branch}" ]]; then
    echo "Current checkout must be on the ${default_branch} branch; found detached HEAD." >&2
    exit 1
  fi
  if [[ "${current_branch}" != "${default_branch}" ]]; then
    echo "Current branch must be ${default_branch}; found ${current_branch}." >&2
    exit 1
  fi

  if ! git merge-base --is-ancestor "refs/remotes/origin/${default_branch}" HEAD; then
    echo "HEAD must contain origin/${default_branch}." >&2
    exit 1
  fi

  head_tag="$(git describe --exact-match --tags HEAD 2>/dev/null || true)"
  if [[ "${head_tag}" != "${tag}" ]]; then
    echo "HEAD must be exactly tagged ${tag}." >&2
    exit 1
  fi

  if ! git merge-base --is-ancestor "${tag}" "refs/heads/${default_branch}"; then
    echo "${tag} must be an ancestor of refs/heads/${default_branch}." >&2
    exit 1
  fi
}

if [[ $# -ne 1 ]]; then
  usage
  exit 1
fi

VERSION="$1"
validate_version "${VERSION}"

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

cd "${ROOT}"

BUNDLE="dist/arez-${VERSION}.zip"
if [[ ! -f "${BUNDLE}" ]]; then
  echo "Missing Maven Central bundle: ${BUNDLE}" >&2
  exit 1
fi

python3 tools/release/verify_bundle.py "${BUNDLE}" "${VERSION}"

if [[ -z "${MAVEN_CENTRAL_USERNAME:-}" ]]; then
  echo "MAVEN_CENTRAL_USERNAME must be set." >&2
  exit 1
fi

if [[ -z "${MAVEN_CENTRAL_PASSWORD:-}" ]]; then
  echo "MAVEN_CENTRAL_PASSWORD must be set." >&2
  exit 1
fi

require_tag_ready "${VERSION}"

BASE_URL="https://central.sonatype.com"
AUTH_TOKEN="$(printf '%s:%s' "${MAVEN_CENTRAL_USERNAME}" "${MAVEN_CENTRAL_PASSWORD}" | base64 | tr -d '\n')"
TMP_DIR="$(mktemp -d)"
trap 'rm -rf "${TMP_DIR}"' EXIT

UPLOAD_RESPONSE="${TMP_DIR}/upload-response.txt"
curl --fail-with-body \
  -H "Authorization: Bearer ${AUTH_TOKEN}" \
  -F "bundle=@${BUNDLE}" \
  -o "${UPLOAD_RESPONSE}" \
  "${BASE_URL}/api/v1/publisher/upload?name=arez-${VERSION}&publishingType=AUTOMATIC"

DEPLOYMENT_ID="$(tr -d '\r\n' < "${UPLOAD_RESPONSE}")"
if [[ -z "${DEPLOYMENT_ID}" ]]; then
  echo "Central Portal upload did not return a deployment id." >&2
  exit 1
fi
echo "Central Portal deployment id: ${DEPLOYMENT_ID}"

STATUS_RESPONSE="${TMP_DIR}/status-response.json"
DEADLINE=$((SECONDS + 1800))
LAST_STATUS=""

while (( SECONDS <= DEADLINE )); do
  if ! curl --fail-with-body \
    -X POST \
    -H "Authorization: Bearer ${AUTH_TOKEN}" \
    -o "${STATUS_RESPONSE}" \
    "${BASE_URL}/api/v1/publisher/status?id=${DEPLOYMENT_ID}"; then
    print_status_failure "Central Portal status request failed." "${DEPLOYMENT_ID}" "${LAST_STATUS}" "${STATUS_RESPONSE}"
    exit 1
  fi

  LAST_STATUS="$(extract_deployment_state "${STATUS_RESPONSE}")"
  echo "Central Portal deployment status: ${LAST_STATUS}"

  case "${LAST_STATUS}" in
    PUBLISHED)
      echo "Central Portal publication succeeded for deployment ${DEPLOYMENT_ID}."
      exit 0
      ;;
    PENDING|VALIDATING|VALIDATED|PUBLISHING)
      sleep 15
      ;;
    FAILED)
      print_status_failure "Central Portal publication failed." "${DEPLOYMENT_ID}" "${LAST_STATUS}" "${STATUS_RESPONSE}"
      exit 1
      ;;
    *)
      print_status_failure "Unexpected Central Portal deployment status." "${DEPLOYMENT_ID}" "${LAST_STATUS}" "${STATUS_RESPONSE}"
      exit 1
      ;;
  esac
done

print_status_failure "Timed out waiting for Central Portal publication." "${DEPLOYMENT_ID}" "${LAST_STATUS}" "${STATUS_RESPONSE}"
exit 1

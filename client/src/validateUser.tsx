export function getAuthToken() {
  return localStorage.getItem('authToken');
}

export function getUsername() {
  return localStorage.getItem('username');
}

export function getUserId() {
  return localStorage.getItem('userId');
}
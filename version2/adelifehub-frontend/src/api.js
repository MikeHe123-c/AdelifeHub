const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'

export function getToken() { return localStorage.getItem('token') || '' }
export function setToken(t) { if (t) localStorage.setItem('token', t); else localStorage.removeItem('token') }

async function request(path, { method = 'GET', headers = {}, body, auth = false } = {}) {
  const opts = { method, headers: { 'Content-Type': 'application/json', ...headers } }
  if (auth) opts.headers['Authorization'] = 'Bearer ' + getToken()
  if (body !== undefined) opts.body = typeof body === 'string' ? body : JSON.stringify(body)
  const res = await fetch(API_BASE + path, opts)
  if (res.status === 401) { setToken(''); throw new Error('UNAUTHORIZED') }
  if (!res.ok) {
    const txt = await res.text(); try { const j = JSON.parse(txt); throw new Error(j.message || txt) } catch { throw new Error(txt || ('HTTP ' + res.status)) }
  }
  const ct = res.headers.get('content-type') || ''
  return ct.includes('application/json') ? res.json() : res.text()
}

export const api = {
  me: () => request('/users/me', { auth: true }),

  meFavorites: (params = {}) =>
    request(`/users/me/favorites?${new URLSearchParams(params)}`, { auth: true }),
  myFavorites: (params = {}) =>
    request(`/users/me/favorites?${new URLSearchParams(params)}`, { auth: true }),
  login: (username, password) => request('/auth/login', { method: 'POST', body: { username, password } }),
  register: (username, email, password) => request('/auth/register', { method: 'POST', body: { username, email, password } }),
  me: () => request('/users/me', { auth: true }),

  listings: (type, page = 1, size = 10) => request(`/listings?${new URLSearchParams({ type, page, size })}`),
  listing: (id) => request(`/listings/${id}`),
  createListing: (payload) => request('/listings', { method: 'POST', auth: true, body: payload }),

  posts: (page = 1, size = 10) => request(`/posts?${new URLSearchParams({ page, size })}`),
  post: (id) => request(`/posts/${id}`),
  createPost: (payload) => request('/posts', { method: 'POST', auth: true, body: payload }),

  listComments: (type, id, page = 1, size = 10) => request(`/${type}s/${id}/comments?${new URLSearchParams({ page, size })}`),
  addComment: (type, id, content) => request(`/${type}s/${id}/comments`, { method: 'POST', auth: true, body: { content } }),

  favListing: (id) => request(`/listings/${id}/favorite`, { method: 'POST', auth: true }),
  unfavListing: (id) => request(`/listings/${id}/favorite`, { method: 'DELETE', auth: true }),
  favPost: (id) => request(`/posts/${id}/favorite`, { method: 'POST', auth: true }),
  unfavPost: (id) => request(`/posts/${id}/favorite`, { method: 'DELETE', auth: true }),

  myFavorites: (params = {}) => request(`/users/me/favorites?${new URLSearchParams(params)}`, { auth: true }),
  updateMe: (patch) => request('/users/me', { method: 'PATCH', auth: true, body: patch }),

  report: (type, id, reasonCode = 'other', reasonText = '') => request(`/${type}s/${id}/report`, { method: 'POST', auth: true, body: { reasonCode, reasonText } }),
}

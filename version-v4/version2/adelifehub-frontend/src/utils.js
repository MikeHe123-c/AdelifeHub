export function timeAgo(iso){
  if(!iso) return ''
  const t = new Date(iso), now = new Date()
  const diff = Math.floor((now - t)/1000)
  if (diff < 60) return `${diff}s`
  if (diff < 3600) return `${Math.floor(diff/60)}m`
  if (diff < 86400) return `${Math.floor(diff/3600)}h`
  return t.toLocaleString()
}
export function cls(...a){ return a.filter(Boolean).join(' ') }

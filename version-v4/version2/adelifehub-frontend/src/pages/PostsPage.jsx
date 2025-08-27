import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'
import { timeAgo } from '../utils'

export default function PostsPage(){
  const [data, setData] = useState([])
  const [meta, setMeta] = useState({ page:1, size:10, total:0 })
  const [err, setErr] = useState('')

  useEffect(()=>{
    api.posts(meta.page, meta.size).then(r=>{ setData(r.data); setMeta(r.meta)}).catch(e=>setErr(e.message))
  }, [meta.page, meta.size])

  return (
    <div>
      <div className="header">Posts</div>
      {err && <div className="warn">{err}</div>}
      <div className="grid">
        {data.map(p=>(
          <Link to={`/posts/${p.id}`} key={p.id} className="card">
            <div style={{fontWeight:700}}>{p.title}</div>
            <div className="small">{timeAgo(p.createdAt)} · ❤️ {p.likes}</div>
            <p className="small">{(p.content || '').slice(0,120)}</p>
          </Link>
        ))}
      </div>
    </div>
  )
}

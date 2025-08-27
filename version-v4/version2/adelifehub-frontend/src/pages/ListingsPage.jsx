import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'
import { timeAgo } from '../utils'

export default function ListingsPage({ type }){
  const [data, setData] = useState([])
  const [meta, setMeta] = useState({ page:1, size:10, total:0 })
  const [err, setErr] = useState('')

  useEffect(()=>{
    api.listings(type, meta.page, meta.size).then(r=>{ setData(r.data); setMeta(r.meta)}).catch(e=>setErr(e.message))
  }, [type, meta.page, meta.size])

  return (
    <div>
      <div className="header">{type==='rental'?'租房':'工作'}</div>
      {err && <div className="warn">{err}</div>}
      <div className="grid">
        {data.map(it=>(
          <Link to={`/listings/${it.id}`} key={it.id} className="card list-item">
            <img src={it.images?.[0] || 'https://via.placeholder.com/96x72?text=IMG'} alt=""/>
            <div>
              <div style={{fontWeight:700}}>{it.title}</div>
              <div className="small">{it.location} · {timeAgo(it.createdAt)}</div>
              {it.price != null && <div><span className="badge">{it.price}{it.priceUnit?`/${it.priceUnit.replace('per_','')}`:''}</span></div>}
            </div>
          </Link>
        ))}
      </div>
    </div>
  )
}

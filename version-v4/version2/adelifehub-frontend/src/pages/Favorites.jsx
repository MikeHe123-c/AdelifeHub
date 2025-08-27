import React, { useEffect, useState } from 'react'
import { api } from '../api'
import { Link } from 'react-router-dom'
import { timeAgo } from '../utils'

export default function Favorites(){
  const [items, setItems] = useState([])
  const [meta, setMeta] = useState({ page:1, size:20, total:0 })

  useEffect(()=>{
    api.myFavorites({ page: meta.page, size: meta.size }).then(r=>{ setItems(r.data); setMeta(r.meta) })
  }, [meta.page, meta.size])

  return (
    <div>
      <div className="header">我的收藏</div>
      <div className="grid">
        {items.map((it,idx)=> (
          <div className="card list-item" key={idx}>
            <img src={it.image || 'https://via.placeholder.com/96x72?text=IMG'} alt=""/>
            <div>
              <div className="small">{it.kind === 'post' ? '帖子' : (it.listingType==='rental'?'租房':'工作')} · {timeAgo(it.favCreatedAt)}</div>
              <div style={{fontWeight:700}}>{it.title}</div>
              <div className="small">{it.location || ''}</div>
              <div className="row" style={{gap:8, marginTop:6}}>
                {it.kind === 'post' ? (
                  <Link className="btn outline" to={`/posts/${it.id}`}>查看</Link>
                ) : (
                  <Link className="btn outline" to={`/listings/${it.id}`}>查看</Link>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

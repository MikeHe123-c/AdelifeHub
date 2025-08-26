import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { api } from '../api'
import { useAuth } from '../auth'
import { timeAgo } from '../utils'

export default function ListingDetail(){
  const { id } = useParams()
  const [it, setIt] = useState(null)
  const [comments, setComments] = useState([])
  const [c, setC] = useState('')
  const { user } = useAuth()
  const [msg, setMsg] = useState('')

  useEffect(()=>{
    api.listing(id).then(setIt).catch(e=>setMsg(e.message))
    api.listComments('listing', id).then(r=>setComments(r.data)).catch(()=>{})
  }, [id])

  async function addComment(){
    if(!c.trim()) return
    try{ const r = await api.addComment('listing', id, c); setComments([r, ...comments]); setC('') }catch(e){ setMsg(e.message) }
  }
  async function fav(){ try{ await api.favListing(id); setMsg('已收藏'); }catch(e){ setMsg(e.message) } }
  async function report(){ try{ await api.report('listing', id, 'other', '不合适'); setMsg('已举报'); }catch(e){ setMsg(e.message) } }

  if(!it) return <div className="container">{msg || 'Loading...'}</div>

  return (
    <div className="grid" style={{gap:16}}>
      <div className="card">
        <h2 style={{marginTop:0}}>{it.title}</h2>
        <div className="small">{it.location} · {timeAgo(it.createdAt)} · 发布者 {it.author?.nickname || it.author?.username}</div>
        <hr/>
        <p>{it.content}</p>
        {it.price != null && <div className="badge">价格：{it.price} {it.priceUnit}</div>}
        <div className="row" style={{gap:8, marginTop:10}}>
          <button className="btn" onClick={fav}>收藏</button>
          <button className="btn outline" onClick={report}>举报</button>
        </div>
      </div>
      <div className="card">
        <div className="header">评论</div>
        {user ? (
          <div className="row" style={{gap:8}}>
            <input className="input" placeholder="写点什么..." value={c} onChange={e=>setC(e.target.value)} />
            <button className="btn" onClick={addComment}>发送</button>
          </div>
        ) : <div className="small">登录后可评论</div>}
        <div style={{marginTop:10}} className="grid">
          {comments.map(cm=>(
            <div key={cm.id} className="card" style={{padding:10}}>
              <div className="small">{timeAgo(cm.createdAt)}</div>
              <div>{cm.content}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

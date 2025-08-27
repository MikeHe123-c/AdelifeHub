import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { api } from '../api'
import { useAuth } from '../auth'
import { timeAgo } from '../utils'

export default function PostDetail(){
  const { id } = useParams()
  const [p, setP] = useState(null)
  const [comments, setComments] = useState([])
  const [c, setC] = useState('')
  const { user } = useAuth()
  const [msg, setMsg] = useState('')

  useEffect(()=>{
    api.post(id).then(setP).catch(e=>setMsg(e.message))
    api.listComments('post', id).then(r=>setComments(r.data)).catch(()=>{})
  }, [id])

  async function addComment(){
    if(!c.trim()) return
    try{ const r = await api.addComment('post', id, c); setComments([r, ...comments]); setC('') }catch(e){ setMsg(e.message) }
  }
  async function fav(){ try{ await api.favPost(id); setMsg('已收藏'); }catch(e){ setMsg(e.message) } }
  async function report(){ try{ await api.report('post', id, 'other', '不合适'); setMsg('已举报'); }catch(e){ setMsg(e.message) } }

  if(!p) return <div>{msg || 'Loading...'}</div>

  return (
    <div className="grid" style={{gap:16}}>
      <div className="card">
        <h2 style={{marginTop:0}}>{p.title}</h2>
        <div className="small">{timeAgo(p.createdAt)} · 作者 {p.author?.nickname || p.author?.username}</div>
        <hr/>
        <p>{p.content}</p>
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

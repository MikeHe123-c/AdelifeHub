import React, { useState } from 'react'
import { api } from '../api'
import { useNavigate } from 'react-router-dom'

export default function NewItem(){
  const [tab, setTab] = useState('post')
  return (
    <div className="grid" style={{gap:12}}>
      <div className="row" style={{gap:8}}>
        <button className={'btn ' + (tab==='post'?'':'outline')} onClick={()=>setTab('post')}>发 Post</button>
        <button className={'btn ' + (tab==='rental'?'':'outline')} onClick={()=>setTab('rental')}>发 Rental</button>
        <button className={'btn ' + (tab==='job'?'':'outline')} onClick={()=>setTab('job')}>发 Job</button>
      </div>
      {tab==='post' && <PostForm/>}
      {tab==='rental' && <ListingForm type="rental"/>}
      {tab==='job' && <ListingForm type="job"/>}
    </div>
  )
}

function PostForm(){
  const nav = useNavigate()
  const [f,setF]=useState({ title:'', content:'', images:[] })
  const [err,setErr]=useState('')
  async function submit(e){ e.preventDefault(); try{ const r=await api.createPost(f); nav('/posts/'+r.id)}catch(ex){setErr(ex.message)} }
  return (
    <form className="card grid" style={{gap:10}} onSubmit={submit}>
      <h3>发布 Post</h3>
      {err && <div className="warn">{err}</div>}
      <input className="input" placeholder="标题" value={f.title} onChange={e=>setF({...f,title:e.target.value})} required/>
      <textarea className="input" rows="6" placeholder="内容" value={f.content} onChange={e=>setF({...f,content:e.target.value})} />
      <button className="btn">发布</button>
    </form>
  )
}

function ListingForm({ type }){
  const nav = useNavigate()
  const [f,setF]=useState({ type, title:'', content:'', price:'', priceUnit:type==='rental'?'per_week':'per_hour', location:'' })
  const [err,setErr]=useState('')
  async function submit(e){ e.preventDefault(); const payload={...f, price: f.price? Number(f.price): null}; try{ const r=await api.createListing(payload); nav('/listings/'+r.id)}catch(ex){setErr(ex.message)} }
  return (
    <form className="card grid" style={{gap:10}} onSubmit={submit}>
      <h3>发布 {type==='rental'?'租房':'工作'}</h3>
      {err && <div className="warn">{err}</div>}
      <div className="form-row"><label>标题</label><input className="input" value={f.title} onChange={e=>setF({...f,title:e.target.value})} required/></div>
      <div className="form-row"><label>描述</label><textarea className="input" rows="6" value={f.content} onChange={e=>setF({...f,content:e.target.value})} /></div>
      <div className="form-row"><label>价格</label><input className="input" value={f.price} onChange={e=>setF({...f,price:e.target.value})} placeholder={type==='rental'?'每周租金':'时薪'}/></div>
      <div className="form-row"><label>单位</label>
        <select className="input" value={f.priceUnit} onChange={e=>setF({...f,priceUnit:e.target.value})}>
          {type==='rental' ? <>
            <option value="per_week">per_week</option>
            <option value="per_month">per_month</option>
          </> : <>
            <option value="per_hour">per_hour</option>
            <option value="per_day">per_day</option>
          </>}
        </select>
      </div>
      <div className="form-row"><label>位置</label><input className="input" value={f.location} onChange={e=>setF({...f,location:e.target.value})} /></div>
      <div><button className="btn">发布</button></div>
    </form>
  )
}

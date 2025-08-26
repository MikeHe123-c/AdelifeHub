import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth'

export default function Login(){
  const { login, register } = useAuth()
  const [isReg, setIsReg] = useState(false)
  const [f, setF] = useState({ username:'', email:'', password:'' })
  const [err, setErr] = useState('')
  const nav = useNavigate()

  async function onSubmit(e){
    e.preventDefault()
    setErr('')
    try{
      if(isReg) await register(f.username, f.email, f.password)
      else await login(f.username, f.password)
      nav('/')
    }catch(ex){ setErr(ex.message) }
  }

  return (
    <div className="card" style={{maxWidth:420, margin:'40px auto'}}>
      <h3 style={{marginTop:0}}>{isReg?'注册':'登录'}</h3>
      {err && <div className="warn">{err}</div>}
      <form onSubmit={onSubmit} className="grid" style={{gap:10}}>
        <input className="input" placeholder="用户名" value={f.username} onChange={e=>setF({...f, username:e.target.value})} required/>
        {isReg && <input className="input" placeholder="邮箱（可选）" value={f.email} onChange={e=>setF({...f, email:e.target.value})}/>}
        <input className="input" placeholder="密码" type="password" value={f.password} onChange={e=>setF({...f, password:e.target.value})} required/>
        <button className="btn" type="submit">{isReg?'注册并登录':'登录'}</button>
      </form>
      <hr/>
      {!isReg ? (
        <div className="small">还没有账号？ <button className="btn outline" onClick={()=>setIsReg(true)}>去注册</button></div>
      ): (
        <div className="small">已有账号？ <button className="btn outline" onClick={()=>setIsReg(false)}>去登录</button></div>
      )}
    </div>
  )
}

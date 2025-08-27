import React, { useEffect, useState } from 'react'
import { useAuth } from '../auth'
import { api } from '../api'
import Avatar from '../components/Avatar'

export default function Profile() {
  const { user, setUser } = useAuth()
  const [f, setF] = useState({ nickname: '', avatarUrl: '', phone: '' })
  const [msg, setMsg] = useState('')

  useEffect(() => {
    if (user) { setF({ nickname: user.nickname || '', avatarUrl: user.avatarUrl || '', phone: user.phone || '' }) }
  }, [user])

  async function save() {
    try { const r = await api.updateMe(f); setUser({ ...user, ...f }); setMsg('已保存') } catch (e) { setMsg(e.message) }
  }

  return (
    <div className="grid" style={{ gap: 16 }}>
      <div className="card row" style={{ gap: 12, alignItems: 'center' }}>
        <Avatar url={f.avatarUrl} name={f.nickname || user.username} size={56} />
        <div><div style={{ fontWeight: 800 }}>{user.username}</div><div className="small">{user.email}</div></div>
        <div className="spacer"></div>
        <button className="btn" onClick={() => location.href = '/me/favorites'}>我的收藏</button>
      </div>
      <div className="card grid" style={{ gap: 10 }}>
        <h3>编辑资料</h3>
        {msg && <div className="success">{msg}</div>}
        <div className="form-row"><label>昵称</label><input className="input" value={f.nickname} onChange={e => setF({ ...f, nickname: e.target.value })} /></div>
        <div className="form-row"><label>头像 URL</label><input className="input" value={f.avatarUrl} onChange={e => setF({ ...f, avatarUrl: e.target.value })} /></div>
        <div className="form-row"><label>电话</label><input className="input" value={f.phone} onChange={e => setF({ ...f, phone: e.target.value })} /></div>
        <div><button className="btn" onClick={save}>保存</button></div>
      </div>
    </div>
  )
}

import React from 'react'
import { Link } from 'react-router-dom'

export default function Home(){
  return (
    <div className="grid cols-3">
      <div className="card">
        <h3>🏠 租房信息</h3>
        <p className="small">Adelaide 合租/整租、短租、配套、地理位置等。</p>
        <Link to="/rentals" className="btn">进入</Link>
      </div>
      <div className="card">
        <h3>💼 工作机会</h3>
        <p className="small">兼职/全职，时薪、公司、签证等信息。</p>
        <Link to="/jobs" className="btn">进入</Link>
      </div>
      <div className="card">
        <h3>🧱 校园墙 Posts</h3>
        <p className="small">拼车、求助、活动、闲聊。</p>
        <Link to="/posts" className="btn">进入</Link>
      </div>
    </div>
  )
}

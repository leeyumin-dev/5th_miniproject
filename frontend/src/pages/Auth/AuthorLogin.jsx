import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // API 호출을 위한 인스턴스
import './AuthForm.css';

export default function AuthorLogin() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!username || !password) {
      alert('아이디와 비밀번호를 모두 입력해주세요.');
      return;
    }

    try {
      const response = await axiosInstance.post('/authors/login', {
        authorLoginId: username,
        authorPw: password,
      });

      //  응답 데이터에서 토큰과 ID를 모두 추출
      //    (백엔드 응답 필드명에 따라 'authorId' 또는 'id' 등으로 수정 필요)
      const token = response.data.token;
      const authorId = response.data.authorId;

      // 2. JWT 토큰과 함께 작가 ID도 localStorage에 저장
      localStorage.setItem('token', token);
      localStorage.setItem('userId', authorId);

      alert('로그인 성공!');

      // 성공 후 작가 메인 페이지로 이동
      navigate('/main/author');

    } catch (err) {
      console.error("Login failed:", err);
      alert('아이디 또는 비밀번호가 틀렸습니다.');
    }
  };

  return (
    <div className="auth-container">
      <h2>작가 로그인</h2>
      <input
        placeholder="아이디"
        value={username}
        onChange={e => setUsername(e.target.value)}
      />
      <input
        type="password"
        placeholder="비밀번호"
        value={password}
        onChange={e => setPassword(e.target.value)}
      />
      <button onClick={handleLogin}>로그인</button>
      <p className="link-text">비밀번호를 잊으셨나요?</p>
    </div>
  );
}
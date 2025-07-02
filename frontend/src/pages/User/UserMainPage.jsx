import React, { useEffect, useState } from 'react';
import axios from 'axios';
import BookCard from '../../components/card/BookCard';
import MyPagePanel from '../../components/layout/MyPagePanel';
import PointChargePanel from '../../components/layout/PointChargePanel';
import SubscribePanel from '../../components/layout/SubscribePanel'; // ✅ 구독 패널 import
import MainLayout from '../../components/layout/MainLayout';
import './UserMainPage.css';
import axiosInstance from "../../api/axiosInstance";

export default function UserMainPage() {
  const [showMyPage, setShowMyPage] = useState(false);
  const [showChargePanel, setShowChargePanel] = useState(false);
  const [showSubscribePanel, setShowSubscribePanel] = useState(false); // ✅ 구독 패널 상태 추가
  const [point, setPoint] = useState(0);
  const [bestsellers, setBestsellers] = useState([]);

  const getBestsellers = async () => {
    try {
      const res = await axiosInstance.get("/books");
      console.log(res.data._embedded.books);
      setBestsellers(res.data._embedded.books);
    } catch (err) {
      console.error("오류: ", err.response?.data);
    }
  };

  const categories = {
    "소설": [{ id: 4, title: "소설책", likes: 370, subscribes: 82 }],
    "판타지": [{ id: 5, title: "판타지책", likes: 370, subscribes: 82 }],
    "경제": [{ id: 6, title: "경제책", likes: 370, subscribes: 82 }]
  };

  const fetchPoint = async () => {
    try {
      const userId = localStorage.getItem('userId');
      const token = localStorage.getItem('token');
      if (!userId || !token) return;

      const res = await axios.get(`/points/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("📦 point 응답 데이터:", res.data);

      if (res.data?.pointSum !== undefined) {
        setPoint(res.data.pointSum);
      } else {
        setPoint(0);
      }
    } catch (err) {
      console.error('포인트 조회 실패:', err);
      setPoint(0);
    }
  };

  useEffect(() => {
    fetchPoint();
  }, []);

  useEffect(() => {
    getBestsellers();
  }, []);

  return (
    <MainLayout>
      <div className="user-main-container">
        <div className="main-left">
          <div className="user-header-panel">
            <h1>걷다가 서재</h1>
            <span>포인트: {point.toLocaleString()}P</span>
            <button onClick={() => setShowMyPage(v => !v)}>
              My Page
            </button>
          </div>

          <h2>이달의 베스트셀러</h2>
          <div className="bestseller-grid">
            {bestsellers.map(book => (
              <BookCard key={book.bookId} book={book} />
            ))}
          </div>

          <h2>카테고리별</h2>
          <div className="category-grid">
            {Object.entries(categories).map(([catName, books]) => (
              <div key={catName} className="category-item">
                <div className="category-label">{catName}</div>
                {books.map(book => (
                  <BookCard key={book.id} book={book} />
                ))}
              </div>
            ))}
          </div>
        </div>

        {/* 마이페이지 패널 */}
        {showMyPage && (
          <div className="main-right">
            <MyPagePanel
              onClose={() => setShowMyPage(false)}
              onChargeClick={() => {
                setShowMyPage(false);
                setShowChargePanel(true);
              }}
              onSubscribeClick={() => {
                setShowMyPage(false);
                setShowSubscribePanel(true); // ✅ 구독 패널 열기
              }}
            />
          </div>
        )}

        {/* 포인트 충전 패널 */}
        {showChargePanel && (
          <div className="main-right">
            <PointChargePanel
              onClose={() => setShowChargePanel(false)}
              onCharged={fetchPoint}
            />
          </div>
        )}

        {/* 정기 구독권 결제 패널 */}
        {showSubscribePanel && (
          <div className="main-right">
            <SubscribePanel
              onClose={() => setShowSubscribePanel(false)}
              onSubscribed={() => {
                alert("구독이 완료되었습니다!");
              }}
            />
          </div>
        )}
      </div>
    </MainLayout>
  );
}

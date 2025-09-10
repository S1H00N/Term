import requests
from bs4 import BeautifulSoup
import pandas as pd
import re

def convert_to_numeric_votes(vote_str):
    """'2.9M', '876K' 같은 문자열을 숫자로 변환하는 함수"""
    vote_str = vote_str.strip().upper()
    if 'M' in vote_str:
        return int(float(vote_str.replace('M', '')) * 1000000)
    elif 'K' in vote_str:
        return int(float(vote_str.replace('K', '')) * 1000)
    else:
        return int("".join(re.findall(r'\d+', vote_str)))

def scrape_movies_by_rank():
    """IMDb Top 250 영화 정보를 랭킹 순서대로 스크레이핑하는 함수"""
    url = "https://www.imdb.com/chart/top"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "Accept-Language": "en-US,en;q=0.5"
    }
    
    try:
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.content, "html.parser")

        movies = []
        movie_list = soup.select('li.ipc-metadata-list-summary-item')

        print(f"✅ 총 {len(movie_list)}개의 영화를 찾았습니다. 데이터 추출을 시작합니다.")
        
        # 찾은 영화 목록(movie_list)을 하나씩 돌면서 정보 추출
        for movie_item in movie_list:
            title_element = movie_item.select_one('h3.ipc-title__text')
            title = title_element.text.split('. ', 1)[1].strip() if title_element else 'N/A'
            
            metadata_elements = movie_item.select('span.sc-b1899a35-8')
            year = metadata_elements[0].text.strip() if len(metadata_elements) > 0 else 'N/A'
            
            rating_element = movie_item.select_one('span.ipc-rating-star')
            if rating_element:
                rating_text = rating_element.text.strip()
                match = re.match(r'([\d\.]+)\s*\(([\d\.\w]+)\)', rating_text)
                if match:
                    rating = float(match.group(1))
                    votes = convert_to_numeric_votes(match.group(2))
                else:
                    rating, votes = 0.0, 0
            else:
                rating, votes = 0.0, 0

            movies.append({
                'title': title,
                'year': int(year) if year.isdigit() else 0,
                'rating': rating,
                'votes': votes
            })
        
        print("✅ 데이터 추출 완료!")
        return pd.DataFrame(movies)

    except requests.exceptions.RequestException as e:
        print(f"❌ 데이터 수집 중 오류가 발생했습니다: {e}")
        return None

# 위에서 정의한 함수를 실제로 실행(호출)하고 결과를 변수에 저장
print("스크레이핑을 시작합니다...")
df = scrape_movies_by_rank()

# 결과가 정상적으로 반환되었는지 확인하고 상위 5개만 출력
if df is not None:
    print("\n--- IMDb Top 5 Movies ---")
    print(df.head())

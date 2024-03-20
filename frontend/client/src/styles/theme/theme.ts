/* 사용방법
    - 사용하려는 페이지에 import
    import theme from '/theme';


    ex) app.css.ts 폴더에 있음
 */


const theme = {
    // 레이아웃
    layout: '#f5f5fa',

    // 헤더
    header: 'rgba(245,245,250,0.8)',

    // 버튼
    blue: '#1892EB',
    blueGray: '#9CB0BE',

    // 박스
    skyblue: '#DDF1FF',
    orange: '#FFEFE0',

    // 그림자
    shadow: '#e8e8e8',


    // 계좌 관련 (이름은 은행명으로 바꾸어도 됨)
    brown: '#8E8070',
    cyan: '#2ECFCB',

    // others
    gray: '#667783',
    line: '#CCDEEB',
        // 즐겨찾기
    yellow: '#FFE483',
    lightGray: '#D2D2D2',

        // 대신내기
    red:  '#F43A51',
    
    // 카카오
    kakao: '#FEE500',
};

export default theme;
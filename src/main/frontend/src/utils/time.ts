export const TimeUtil = {
    getLastAgo: (str: string | undefined) => {
        const timestamp = str ? new Date(str).getTime(): Date.now();
        const timeDiff = Date.now() - timestamp;

        // 밀리초를 분, 시, 일 단위로 변환
        const minutesDiff = Math.floor(timeDiff / (1000 * 60));
        const hoursDiff = Math.floor(timeDiff / (1000 * 60 * 60));
        const daysDiff = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

        if (minutesDiff < 1) {
            return `${timeDiff / 1000}초 전`
        }
        else if (hoursDiff < 1) {
            return `${minutesDiff}분 전`;
        }
        else if (daysDiff < 1) {
            return `${daysDiff}일 전`
        }
        return `${timeDiff / 1000}초 전`
    }
}
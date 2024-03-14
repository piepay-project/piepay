'use client'

import {useRouter} from "next/navigation";
import * as styles from "@/styles/payment/select/selectMember.css";

type Props={
    meetId: string
}

export default function ParticipateButton({ meetId }: Props) {
    const route = useRouter();
    const onClickReplace = () => {
        route.replace(`/${meetId}/payment/approve`);
    }
    return(
    <button onClick={onClickReplace} className={styles.submitButton}>
        <div>알림 보내기</div>
    </button>
    )
}
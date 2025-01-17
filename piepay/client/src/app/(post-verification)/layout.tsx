import type {Metadata} from "next";
import Header from "./component/Header"
import {ReactNode} from "react";
import {dehydrate, HydrationBoundary, QueryClient} from "@tanstack/react-query";
import {getAccount} from "@/api/account";
import NotificationReceive from "@/app/(post-verification)/component/NotificationReceive";
import * as styles from "@/styles/main/mainLayout.css"
import {cookies} from "next/headers";
import {getMeetList} from "@/api/meet/meetList";
import { getNotification } from '@/api/notification';
import { getMe } from '@/api/member';
import { getCurrPayment } from '@/api/payment';

export const metadata: Metadata = {
    title: "piepay",
    description: "Generated by create next app",
};

type Props = { children: ReactNode, modal: ReactNode }


export default async function PostVerificationLayout({children}: Props) {
    const queryClient = new QueryClient();
    const token = cookies().get('accessToken')?.value;

    await queryClient.prefetchQuery({queryKey: ['account', token], queryFn: getAccount, staleTime: 1000 * 60 * 60});
    await queryClient.prefetchQuery({queryKey: ['meetList', token], queryFn: getMeetList, staleTime: 1000 * 60 * 15});
    await queryClient.prefetchQuery({queryKey: ['notification', token], queryFn: getNotification});
    await queryClient.prefetchQuery({queryKey: ['userInfo', token], queryFn: getMe,staleTime: 1000* 60 * 60},);
    await queryClient.prefetchQuery({queryKey: ['currPayment', token], queryFn: getCurrPayment, staleTime: 1000 * 60 * 100});
    const dehydratedState = dehydrate(queryClient);


    return (
        <>
                <Header/>
                <HydrationBoundary state={dehydratedState}>
                    <NotificationReceive/>
                    <div className={styles.mainContainer}>

                        {children}
                    </div>
                </HydrationBoundary>
        </>
    );
}

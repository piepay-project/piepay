import type {Metadata} from "next";
import {ReactNode} from "react";
import * as styles from "@/styles/payment/select/payment.css"
import {dehydrate, HydrationBoundary, QueryClient} from "@tanstack/react-query";
import { getMe, getMembers } from '@/api/member';
import {getAccount} from "@/api/account";
import { cookies } from 'next/headers';
import { getMyMeets } from '@/api/meet';
import RQProvider from '@/app/(post-verification)/component/RQProvider';
import Header from '@/app/(post-verification)/component/Header';
import NotificationReceive from '@/app/(post-verification)/component/NotificationReceive';

export const metadata: Metadata = {
    title: "Create Next App",
    description: "Generated by create next app",
};

type Props = {
    children: ReactNode,
}

export default async function MyPage({children}: Props) {
    const queryClient = new QueryClient();
    const token = cookies().get('accessToken')?.value;
    console.log(token);
    await queryClient.prefetchQuery({queryKey: ['me', token], queryFn: getMe});

    const dehydratedState = dehydrate(queryClient);
    return (
      <div className="h-[100%]">
              <HydrationBoundary state={dehydratedState}>
                      {children}
              </HydrationBoundary>
      </div>
    )
}

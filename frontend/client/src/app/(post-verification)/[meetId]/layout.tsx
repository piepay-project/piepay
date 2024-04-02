import type {Metadata} from "next";
import {ReactNode} from "react";
import {dehydrate, HydrationBoundary, QueryClient} from "@tanstack/react-query";
import {getMeetInfo, getMeetPayments} from "@/api/meet";
import {cookies} from "next/headers";
import {getMeetMemberList} from "@/api/meet/memberList";


type Props = {
    children: ReactNode,
    params: { meetId: string },
}

export default async function PaymentModalLayout({children, params}: Props) {
    const token = cookies().get('accessToken');

    const {meetId} = params;
    const queryClient = new QueryClient();
    await queryClient.prefetchQuery({queryKey: ['meetMembers', meetId, token?.value], queryFn: getMeetMemberList, staleTime: 1000 * 60 * 10});
    await queryClient.prefetchQuery({queryKey: ['meetInfo', meetId, token?.value], queryFn: getMeetInfo, staleTime: 1000 * 60 * 10});
    await queryClient.prefetchQuery({queryKey: ['meetPayments', meetId, token?.value], queryFn: getMeetPayments, staleTime: 1000 * 60 * 10});

    const dehydratedState = dehydrate(queryClient);


    return (
        <>
            <HydrationBoundary state={dehydratedState}>
                {children}
            </HydrationBoundary>
        </>
    );
}

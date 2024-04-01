"use client";


import {useRouter} from "next/navigation";


export default function Success({
                                    searchParams
                                }: {
    searchParams : { [key: string]: string | string[] | undefined }
}) {
    const router = useRouter();
    const accessToken = searchParams.accessToken;

    const setToken = async () => {

        if (typeof accessToken === 'string') {
            document.cookie = `accessToken=${accessToken}`;
            // refreshRequest(accessToken);
            router.push('/sign-in/personal-auth');
        } else {
            router.back();
        }
    }

    setToken();

    return (
      <div>
          로그인 완료!
      </div>
    )
}
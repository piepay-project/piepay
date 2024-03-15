import React, { SVGProps } from "react";
import theme from "@/styles/theme/theme";

interface SvgComponentProps extends SVGProps<SVGSVGElement> {
    color?: string;
}

const NotificationIcon: React.FC<SvgComponentProps> = ({color, ...props}) => (
    <svg
        xmlns="http://www.w3.org/2000/svg"
        width={36}
        height={36}
        viewBox="0 -960 960 960"
        {...props}
    >
        <path d="M200-200q-17 0-28.5-11.5T160-240q0-17 11.5-28.5T200-280h40v-280q0-83 50-147.5T420-792v-28q0-25 17.5-42.5T480-880q25 0 42.5 17.5T540-820v28q80 20 130 84.5T720-560v280h40q17 0 28.5 11.5T800-240q0 17-11.5 28.5T760-200H200ZM480-80q-33 0-56.5-23.5T400-160h160q0 33-23.5 56.5T480-80Z"
              fill={color || theme.blueGray}/>
    </svg>
);

export default NotificationIcon;
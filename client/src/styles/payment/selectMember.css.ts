import { style } from '@vanilla-extract/css'
import theme from '@/styles/theme/theme';

export const container = style({
    width: "100%",
})
export const contentContainer = style({
    top: "5"
})
export const progress = style({
    // Mobile width
    width: '100%',

    // Default width
    '@media': {
        'screen and (min-width: 768px)': {
            width: '40%',
            left: '30%',
        },
    },
    position: 'fixed',
    zIndex: 1,
    backgroundColor: theme.layout,
    top: 0,
    opacity: '80%',
})

export const headerContainer = style({
    width: '100%',
    position: 'sticky',
    backgroundColor: theme.layout,
    top: 5,
    paddingLeft: '15px',
    paddingRight: '15px',
    paddingTop: '20px',

})

export const header = style({
    display: 'flex',
    justifyContent: 'space-between',
    marginBottom: '20px',
    paddingRight: '60px',
})

export const title = style({
    fontWeight: 'bold',
    fontSize: '25px'
})


export const selectedContainer = style({
    display: 'flex',
    marginTop: '15px',
    marginBottom: '10px',
})

export const image = style({
    borderRadius: '20px',
    margin: '7px'
})

export const hr = style({
    border: 0,
    height: '2px',
    background: theme.line,
    marginBottom: '20px',
})


export const listHeaderContainer = style({
    zIndex: 1,
    top: 32,
    position: 'sticky',
    backgroundColor: theme.layout,
    opacity: '80%'
})
export const memberContainer = style({
    position: 'sticky',
    overflowY: 'auto',
    paddingBottom: '100px',
    backgroundColor: theme.layout,

})

export const submitButton = style({
    width: '90%',
    height: '10%',
    left: '5%',
    // Default width
    '@media': {
        'screen and (min-width: 768px)': {
            width: '30%',
            left: '35%'
        },
    },
    background: theme.blue,
    alignItems: 'center',
    borderRadius: '10px',
    position: 'fixed',
    display: 'flex',
    justifyContent: 'center',
    bottom: '30px',
    color: 'white',
    fontWeight: 'bold'
})

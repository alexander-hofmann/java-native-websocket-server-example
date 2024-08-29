import {useState, useEffect} from 'react';

//  rfc6455: https://datatracker.ietf.org/doc/html/rfc6455

function WebSocketClient(){
    const [hello, setHello] = useState('NOBODY');

    useEffect(()=>{
        const reconnect = () => {
            let socket = undefined;

            socket = new WebSocket('http://localhost:8089');

            socket.onerror = () => {
                console.log('websocket error...');
            };
            socket.onopen = () => {
                console.log('websocket connection established...');
                socket.send("world")
                setTimeout(()=> {
                    socket.send('datetime');
                }, 10000);
            };
            socket.onmessage = (event) => {
                console.log(event.data);
                setHello(event.data);
            }
            socket.onclose = () => {
                console.log('WebSocket connection closed.');
            };
        }
        reconnect();
    },[]);


    return (
        <div>HELLO... {hello}</div>
    )
}

export default WebSocketClient;
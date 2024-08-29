import {useState, useEffect} from 'react';
import Dygraph from 'dygraphs';
import 'dygraphs/dist/dygraph.min.css'

//  rfc6455: https://datatracker.ietf.org/doc/html/rfc6455

function parseISOString(s) {
    var b = s.split(/\D+/);
    return new Date(Date.UTC(b[0], --b[1], b[2], b[3], b[4], b[5], b[6]));
}

function WSGraph(){
    const [name, setName] = useState("graph")
    const [values, setValues] = useState(null);

    useEffect(()=>{
        var g = null;

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
                    socket.send('sinus');
                }, 10000);
            };
            socket.onmessage = (event) => {
                try {
                    var json = JSON.parse(event.data)
                    console.log(json);
                    setValues((oldvalues) => {
                        var newValues = null;
                        if (oldvalues === null) {
                            newValues = [[new Date(json.date), json.value]]
                        } else {
                            newValues = [...oldvalues,
                                [parseISOString(json.date), json.value]
                            ];
                        }
                        if (g === null) {
                            g = new Dygraph(document.getElementById(name), newValues,
                                {
                                    resizable: "both",
                                    rollPeriod: 365,
                                    drawPoints: true,
                                    showRoller: true,
                                    valueRange: [-1.2, 1.2],
                                    labels: ['Time', 'Sinus']
                                });
                        }
                        g.updateOptions({'file': newValues})
                        return newValues;
                    });
                } catch (e) {
                    //console.log(e);
                    console.log(event.data)
                }
            }
            socket.onclose = () => {
                console.log('WebSocket connection closed.');
            };
        }
        reconnect();
    },[name]);


    return (
        <div
            id="graph"
        >
        </div>
    )
}

export default WSGraph;
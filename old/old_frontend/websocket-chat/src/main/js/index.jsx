import React from "react";
import ReactDOM from "react-dom";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            name: "Anonymous",
            text: "",
            messages: null
        };

        this.onNameChange = this.onNameChange.bind(this);
        this.onTextChange = this.onTextChange.bind(this);
        this.sendMsg = this.sendMsg.bind(this);
    }

    componentDidMount(){
        const socket = new SockJS('/websocket-endpoint');

        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({},  () => {
            console.log('WS connected');

            //subscribe to a specific topic
            this.stompClient.subscribe('/topic/messages', msg =>  {
                //callback for when we receive a message from that topic
                const dto = JSON.parse(msg.body);

                this.setState(
                    prev => {
                        if(prev.messages === null){
                            return {messages: [dto]};
                        } else {
                            return {messages: [...prev.messages, dto]};
                        }
                    }
                );
            });
        });
    }


    sendMsg(){

        const dto = {author: this.state.name, text: this.state.text};

        // WS can also be used to send messages to the server
        this.stompClient.send("/ws-api/message", {}, JSON.stringify(dto));

        //reset text after sending a message
        this.setState({text: ""});
    }

    onNameChange(event) {
        this.setState({name: event.target.value});
    }

    onTextChange(event){
        this.setState({text: event.target.value});
    }


    render() {

        let messages = <div></div>;

        if(this.state.messages !== null){
            messages = <div>
                {this.state.messages.map(m =>
                    <p key={"msg_key_" + m.id}> {m.author + ": " + m.text}</p>
                )}
            </div>;
        }

        return (
            <div>
                <h2>WebSocket-based Chat</h2>
                <div>
                    <p className="inputName">Your name:</p>
                    <input type="text"
                           className="inputName"
                           value={this.state.name}
                           onChange={this.onNameChange}/>
                </div>
                <br/>
                <div>
                    <p>Your message:</p>
                    <textarea  cols="50"
                               rows="5"
                               value={this.state.text}
                               onChange={this.onTextChange} />
                </div>
                <br/>

                <div id="sendId" className="btn" onClick={this.sendMsg}>Send</div>
                {messages}
            </div>
        );
    }
}

ReactDOM.render(<App/>, document.getElementById("root"));
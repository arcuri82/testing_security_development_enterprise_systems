var stompClient = null;

// create WS connection
function connect() {
    var socket = new SockJS('/websocket-endpoint');

    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log('WS connected');

        //subscribe to a specific topic
        stompClient.subscribe('/topic/messages', function (msg) {
            //callback for when we receive a message from that topic
            var dto = JSON.parse(msg.body);
            updateMessages(dto)
        });
    });
}


function updateMessages(dto){

    var msgDiv = document.getElementById('messagesId')

    var p = "<p>" + dto.author + ": " + dto.text + "</p>"

    msgDiv.innerHTML += p
}


function sendMsg(){

    var authorDiv = document.getElementById('nameId')
    var msgDiv = document.getElementById('msgAreaId')

    var dto ={author: authorDiv.value, text: msgDiv.value}

    // WS can also be used to send messages to the server
    stompClient.send("/ws-api/message", {}, JSON.stringify(dto));
}


connect()
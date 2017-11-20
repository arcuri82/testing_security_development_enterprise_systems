var stompClient = null;


function connect() {
    var socket = new SockJS('/websocket-endpoint');

    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log('WS connected');

        stompClient.subscribe('/topic/messages', function (msg) {
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

    stompClient.send("/ws-api/message", {}, JSON.stringify(dto));
}


connect()
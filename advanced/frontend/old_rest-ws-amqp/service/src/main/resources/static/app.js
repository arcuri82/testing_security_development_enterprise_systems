var stompClient = null;

var counter = 0

function connect() {
    var socket = new SockJS('/websocket-endpoint');

    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log('WS connected');

        stompClient.subscribe('/topic/restcalls', function (msg) {
            counter++
            var counterSpan = document.getElementById('counterId')
            counterSpan.innerHTML = counter
        });
    });
}


function doRestCall(){

    var post = new XMLHttpRequest()
    post.open("POST", "/ui/api/foo", true)

    post.send()
}


connect()
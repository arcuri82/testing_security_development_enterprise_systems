// Note: very, very basic (and bad) JS code, with no validations nor exception handling

sendToZuul = function (idText, idMessages) {

    var textArea = document.getElementById(idText)

    var post = new XMLHttpRequest()
    post.open("POST", "/service/messages", true)
    post.setRequestHeader("Content-type", "text/plain")

    post.onload = function () {
        updateMessages(idMessages)
    }

    post.send(textArea.value)

    textArea.value = ""
}

deleteMessages = function(idMessages){

    var del = new XMLHttpRequest()
    del.open("DELETE", "/service/messages", true)

    del.onload = function () {
        updateMessages(idMessages)
    }

    del.send()
}

updateMessages = function(idMessages){
    var get = new XMLHttpRequest()
    get.open("GET", "/service/messages", true)
    get.setRequestHeader("Accept", "application/json;charset=UTF-8")

    get.onload = function () {
        var messages = JSON.parse(this.responseText);

        var msgDiv = document.getElementById(idMessages)

        msgDiv.innerHTML = ''

        var list = "<ul>"

        messages.forEach(function (item, index) {
            //Note: security-wise, this is really bad, as open
            //to XSS attacks
            list += "<li>" + item.system + " : " + item.message +"</li>"
        })

        list += "</ul>"

        msgDiv.innerHTML = list
    }

    get.send()
}
"use strict";

var names = ["Chung", "Ellie", "Albert", "Karoline", "Brendan", "Julia"];
var chat;

function generateRandomName() {
    var index = Math.floor(Math.random() * names.length);
    return names[index];
}

class ChatRoom {
    constructor() {
        this.bot = new ChatBot(this, generateRandomName());
        this.bot.welcomeMessage();
    }

    createUserMessage(message) {
        $(".chat-ul").append("<li><div class='message-data'><span class='message-data-name'>"
         + "<i class='fa fa-circle you'></i>You</span></div><div class='message you-message'>"
         + message + "</div></li>");

         this.scrollDown();
    }

    createBotMessage(message) {
        $(".chat-ul").append("<li class='clearfix'><div class='message-data align-right'>"
         + "<span class='message-data-name'>" + this.bot.name + ", your InciManager assistant"
         + "</span><i class='fa fa-circle me'></i></div><div class='message me-message "
         + "float-right'>" + message + "</div></li>");

        this.scrollDown();
    }

    onNewUserMessage() {
        var message = $("textarea").val();
        if (message != "") {
            this.createUserMessage(message);
            this.bot.onNewUserMessage(message);

            // remove last message from textarea
            $("textarea").val("");
        }
    }

    scrollDown() {
        $(".chat-history").scrollTop($(".chat-history")[0].scrollHeight);
    }
}

$(document).ready(function() {
    chat = new ChatRoom();

    $("#textArea").keypress(function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code == 13) {
            $("#sendMsgButton").trigger('click');
            return false;
        }
    });
});

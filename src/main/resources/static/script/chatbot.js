"use strict";

var dialogFlowAPI = '4054726dff944193a7fcb8b9c8c203ef';
var dialogFlowUrl = 'https://api.dialogflow.com/v1/';

class ChatBot {

    constructor(chatRoom, name) {
        this.chatRoom = chatRoom;
        this.name = name;
    }

    onNewUserMessage(message) {
        $.ajax({
            type: 'POST',
            url: dialogFlowUrl + "query",
            contentType: "application/json; charset=utf-8",
            datatype: "json",
            headers: {
                "Authorization": "Bearer " + dialogFlowAPI
            },
            data: JSON.stringify({ query: message, lang: "es", sessionId: "12345" }),
            success: function(data) {
                this.processAnswer(data);
            }.bind(this), error: function() {
                this.chat.createBotMessage("This service is under maintainance. Please come back in a few minutes.");
            }
        });
    }

    processAnswer(data) {
        console.log(data);
        var answer = data.result.speech;
        this.chatRoom.createBotMessage(answer);
    }

}

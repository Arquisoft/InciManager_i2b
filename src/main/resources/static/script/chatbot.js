"use strict";

var dialogFlowAPI = '4054726dff944193a7fcb8b9c8c203ef';
var dialogFlowUrl = 'https://api.dialogflow.com/v1/';

class ChatBot {

    constructor(chatRoom, name) {
        this.chatRoom = chatRoom;
        this.name = name;
        this.session = Math.floor(Math.random() * 10000);
        this.incident = {
            "agent": {
                "username": agentInfo.username,
                "password": agentInfo.password,
                "kind": agentInfo.kind
            },
            "location": {

            },
            "moreInfo": [],
            "properties": {

            }
        };
    }

    welcomeMessage() {
        this.chatRoom.createBotMessage("Hello, welcome to the official InciManager "
                                        + "support service. How can I help you?");
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
            data: JSON.stringify({ query: message, lang: "en", sessionId: String(this.session) }),
            success: function(data) {
                this.processAnswer(data);
            }.bind(this), error: function() {
                this.chat.createBotMessage("This service is under maintainance. Please come back in a few minutes.");
            }
        });
    }

    processAnswer(data) {
        var action = data.result.action;
        var answer = data.result.speech;

        switch (action) {
            case "incident_name":
                this.incident.inciName = data.result.parameters.name;
                break;

            case "incident_scan_location":
                navigator.geolocation.getCurrentPosition(function(position) {
                    this.incident.location.lat = position.coords.latitude;
                    this.incident.location.lon = position.coords.longitude;

                    this.chatRoom.createBotMessage("Location scanned. Lat: "
                            + this.incident.location.lat + " - Lon: "
                            + this.incident.location.lon
                    );

                    this.chatRoom.createBotMessage("Now you can introduce some"
                            + " tags for the incident.");
                }.bind(this), function(error) {
                    this.processAnswer(data);
                }.bind(this));
                break;

            case "incident_latitude_given":
                this.incident.location.lat = data.result.parameters.latitude;
                this.incident.location.lon = data.result.parameters.longitude;
                break;

            case "incident_tags":
                this.incident.tags = data.result.parameters.tags;
                break;

            case "incident_properties":
                var key = data.result.parameters.key;
                var value = data.result.parameters.value;
                if (key !== null && value !== null) {
                    this.incident.properties[key] = value;
                }
                this.createIncident();
                break;
        }

        if (answer !== "") {
            this.chatRoom.createBotMessage(answer);
        }
    }

    createIncident() {
        var url = window.location.href;
        var arr = url.split("/");

        $.ajax({
            type: 'POST',
            url: arr[0] + "//" + arr[2] + "/incident/create",
            contentType: "application/json; charset=utf-8",
            datatype: "json",
            data: JSON.stringify(this.incident),
            success: function(data) {
                var message = `The incident has been sent:
                    Name: ${this.incident.inciName}
                    Agent: ${this.incident.agent.username}
                    Location: ${this.incident.location}
                    Tags: ${this.incident.tags}
                    Properties: ${this.incident.properties}
                `;
                this.chatRoom.createBotMessage(message);
            }.bind(this), error: function() {
                this.chatRoom.createBotMessage("There was an error \
                sending the incident. Please try again later.");
            }.bind(this)
        });
    }

}

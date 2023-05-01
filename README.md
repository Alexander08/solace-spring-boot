# Spring boot with Solace 

This repository contains 2 projects:
- Solace producer
- Solace consumer

The setup makes use of docker-compose to start a local Solace broker

## Configuration 

The project is configured like this:

Producer: 
- Is pub/sub
- will produce messages of type `SomeMessage` or `OtherMessage` and it will publish them to the topic
```
  domain.org/SomeMessage/{id}
  domain.org/OtherMessage/{id}
```

Consumer:
- listen to the queues
```
"SOME_MESSAGE_QUEUE"
"OTHER_MESSAGE_QUEUE"
```

Broker:
- create the queues with subscriptions
```
"SOME_MESSAGE_QUEUE": "domain.org/SomeMessage/>"
"OTHER_MESSAGE_QUEUE": "domain.org/OtherMessage/>"
```
- the `/>` means that will listen for everything after messageType


### Solace configuration

Let's start very easy configuration. For default vpn 'default-vpn' we will create the queues

- open console: http://localhost:8080
- open default vpn
- open queues menu tab
- create new queues
- create subscription

Or call the script `create-queues-and-subscriptions.sh`

That's it!

## Run project:

You will need docker for this, but after that is as simple as running this command. which will start all 3 services:

```shell
docker-compose up --build --force-recreate
```

***Note!*** be sure you build the projects before starting docker-compose.

## Troubleshooting

- Keep in mind that without the queue created the spring boot application will throw errors.
To resolve this, just create the queues

- Sometimes the solace-broker does not work, that's because docker does not have enough resources to run the container
  - Try to give docker at least 6gb of memory and 6cpu

- If you implement a Deserializer for messages, keep in mind that Solace has default property `JMS_Solace_isXML:true`
  - some objectMappers will fail to deserialize
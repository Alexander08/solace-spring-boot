#!/usr/bin/env bash

# Command to create the queues
curl -X POST http://localhost:8080/SEMP/v2/config/msgVpns/default/queues \
-u admin:admin \
-H 'Content-type: application/json' \
-d '{ "queueName":"SOME_MESSAGE_QUEUE","accessType":"non-exclusive","maxMsgSpoolUsage":50,"permission":"consume","ingressEnabled":true,"owner": "default","egressEnabled":true}'

curl -X POST http://localhost:8080/SEMP/v2/config/msgVpns/default/queues \
-u admin:admin \
-H 'Content-type: application/json' \
-d '{ "queueName":"OTHER_MESSAGE_QUEUE","accessType":"non-exclusive","maxMsgSpoolUsage":50,"permission":"consume","ingressEnabled":true,"owner": "default","egressEnabled":true}'

# Now let's create the subscriptions:
curl -X POST http://localhost:8080/SEMP/v2/config/msgVpns/default/queues/SOME_MESSAGE_QUEUE/subscriptions \
-u admin:admin \
-H 'Content-type: application/json' \
-d '{"msgVpnName": "default", "queueName": "SOME_MESSAGE_QUEUE", "subscriptionTopic": "domain.org/SomeMessage/>"}'

curl -X POST http://localhost:8080/SEMP/v2/config/msgVpns/default/queues/OTHER_MESSAGE_QUEUE/subscriptions \
-u admin:admin \
-H 'Content-type: application/json' \
-d '{"msgVpnName": "default", "queueName": "OTHER_MESSAGE_QUEUE", "subscriptionTopic": "domain.org/OtherMessage/>"}'


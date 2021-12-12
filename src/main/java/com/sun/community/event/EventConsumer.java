package com.sun.community.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.community.entity.Event;
import com.sun.community.entity.Message;
import com.sun.community.service.MessageService;
import com.sun.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})
    public void handleCommentMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息的内容为空");
            return;
        }

        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            logger.error("消息格式错误！");
            return;
        }

        //发送站内通知
        Message message=new Message();
        message.setFromId(SYSTEM_USRE_ID);
        message.setToId(event.getEntityId());
        message.setContent(event.getTopic());
        message.setCreateTime(new Date());
        message.setConversationId(record.topic());

        //构建通知内容
        Map<String,Object> content=new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("getEntityId",event.getEntityId());

        if(!event.getData().isEmpty()){
            for(Map.Entry<String,Object> entry:event.getData().entrySet()){
                content.put(entry.getKey(),entry.getValue());
            }
        }

        message.setContent(JSON.toJSONString(content));
        messageService.addMessage(message);
    }

}

package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.util.ArrayList;
import java.util.List;

public class MessageWall_and_RemoteLogin_Impl implements RemoteLogin, MessageWall {

    private List<Message> messages;
    
       public MessageWall_and_RemoteLogin_Impl() {
        this.messages = new ArrayList<Message>();
       }

    @Override
    public UserAccess connect(String usr, String passwd) {
        return new UserAccess_Impl(this, usr);
    }

    @Override
    public void put(String user, String msg) {
        messages.add(new Message_Impl(user,msg));
    }

    @Override
    public boolean delete(String user, int index) {
        return messages.remove(messages.get(index)); 
    }

    @Override
    public Message getLast() {
        if(messages.size() > 0)
        {
            return messages.get(messages.size()-1);
        }
        return new Message_Impl("NO user", "No message");
    }

    @Override
    public int getNumber() {
        return messages.size();
    }

    @Override
    public List<Message> getAllMessages() {
        return messages; 
    }

    
}

package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;
import java.util.List;

public class SocialMediaService {
    SocialMediaDAO dao;

    public SocialMediaService(){
        dao = new SocialMediaDAO();
    }

    public SocialMediaService(SocialMediaDAO dao){
        this.dao = dao;
    } 

    public Account createAccount(Account acc){
        String username = acc.getUsername();
        String password = acc.getPassword();

        if(username == "") return null;
        if(password.length() < 4) return null;
        if(dao.findAccount(username)) return null;

        return dao.createAccount(acc);
    }

    public Account login(Account acc){
        return dao.findAccount(acc.getUsername(), acc.getPassword());
    }

    public Message postMessage(Message m){
        String text = m.getMessage_text();
        int from = m.getPosted_by();

        if(text.length() == 0 || text.length() > 255) return null;
        if(!dao.findAccount(from)) return null;

        return dao.postMessage(m);
    }

    public List<Message> getMessages(){
        return dao.getMessages();
    }

    public Message getMessage(int id){
        return dao.getMessage(id);
    }

    public Message deleteMessage(int id){
        Message m = dao.getMessage(id);
        dao.deleteMessage(id);
        return m;
    }

    public Message updateMessage(int id, String text){
        if(text.length() == 0 || text.length() > 255) return null;
        
        Message m = dao.getMessage(id);
        if(m == null) return null;

        dao.updateMessage(id, text);
        return new Message(id, m.getPosted_by(), text, m.getTime_posted_epoch());
    }

    public List<Message> getMessages(int id){
        return dao.getMessages(id);
    }
}

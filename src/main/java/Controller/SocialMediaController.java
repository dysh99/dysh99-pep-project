package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService service = new SocialMediaService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        
        // #1 new user registration
        app.post("register", this::registerHandler);

        // #2 user login
        app.post("login", this::loginHandler);

        // #3 new message
        app.post("messages", this::createMessageHandler);

        // #4 retrieve all messages
        app.get("messages", this::getMessagesHandler);

        // #5 retrieve message by id
        app.get("/messages/{message_id}", this::getMessageByIDHandler);

        // #6 delete message by id
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        // #7 update message
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        // #8 retrieve messages by user
        app.get("/accounts/{account_id}/messages", this::retrieveMessageByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account ret = service.createAccount(account);
        if(ret == null){
            //not successful
            ctx.status(400);
        }else{
            //successful
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(ret));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account ret = service.login(acc);
        if(ret == null){
            //not successful
            ctx.status(401);
        }else{
            //successful
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(ret));
        }
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message m = mapper.readValue(ctx.body(), Message.class);
        Message ret = service.postMessage(m);
        if(ret == null){
            //not successful
            ctx.status(400);
        }else{
            //successful
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(ret));
        }
    }

    private void getMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        ctx.status(200);
        ctx.json(mapper.writeValueAsString(service.getMessages()));
    }

    private void getMessageByIDHandler(Context ctx){
        String message_ID = ctx.pathParam("message_id");
        try {
            int id = Integer.parseInt(message_ID);
            Message m = service.getMessage(id);
            if(m != null){
                ObjectMapper mapper = new ObjectMapper();
                ctx.json(mapper.writeValueAsString(m));
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        ctx.status(200);
    }

    private void deleteMessageHandler(Context ctx){
        String message_ID = ctx.pathParam("message_id");
        try {
            int id = Integer.parseInt(message_ID);
            Message m = service.deleteMessage(id);
            if(m != null){
                ObjectMapper mapper = new ObjectMapper();
                ctx.json(mapper.writeValueAsString(m));
            }    
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        ctx.status(200);        
    }

    private void updateMessageHandler(Context ctx){
        String message_ID = ctx.pathParam("message_id");
        try {
            int id = Integer.parseInt(message_ID);

            ObjectMapper mapper = new ObjectMapper();
            Message m = mapper.readValue(ctx.body(), Message.class);

            Message ret = service.updateMessage(id, m.getMessage_text());
            if(ret == null){
                ctx.status(400);
            }else{
                ctx.status(200);
                ctx.json(mapper.writeValueAsString(ret));
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }            
    }

    private void retrieveMessageByUserHandler(Context ctx){
        String account_ID = ctx.pathParam("account_id");
        try {
            int id = Integer.parseInt(account_ID);

            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(service.getMessages(id)));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        ctx.status(200);
    }
}
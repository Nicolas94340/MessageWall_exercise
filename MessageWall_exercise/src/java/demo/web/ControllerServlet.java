package demo.web;

import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        String view = perform_action(request);
        forwardRequest(request, response, view);
    }

    protected String perform_action(HttpServletRequest request)
        throws IOException, ServletException {
        
        String serv_path = request.getServletPath();
        HttpSession session = request.getSession();

        if (serv_path.equals("/login.do")) {
            //...
             String login = request.getParameter("user");
             String password = request.getParameter("password");
             if(login != null && password != null)
             {
                 UserAccess ua = getRemoteLogin().connect(login, password);
                 if(ua != null)
                 {
                     session.setAttribute("useraccess", ua);
                     return "/view/wallview.jsp";
                 }
             }
            return "/error-no-user_access.html";
        } 
        
        else if (serv_path.equals("/put.do")) {
            //...
            UserAccess ua =(UserAccess) session.getAttribute("useraccess");
            if(ua!=null)
            {
                String msg = request.getParameter("msg");
                ua.put(msg);
                return "/view/wallview.jsp";
                
            }
            return "/error-not-loggedin.html";
        } 
        
        else if (serv_path.equals("/refresh.do")) {
            //...
            UserAccess ua =(UserAccess) session.getAttribute("useraccess");
            if(ua != null)
            {
                return "/view/wallview.jsp";
            }
            return "/error-not-loggedin.html";
        } 
        
        else if (serv_path.equals("/delete.do")) {
            //...
                UserAccess ua =(UserAccess) session.getAttribute("useraccess");    
                if(ua != null)
            {
                String index = request.getParameter("index");
                ua.delete(Integer.valueOf(index));
                return "/view/wallview.jsp";
            }
                
                return "/goodbye.html";
        } 
        
        else if (serv_path.equals("/logout.do")) {
            //...
                session.removeAttribute("sessionUser");
                return "/goodbye.html";
        } 
        
        else {
            return "/error-bad-action.html";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }
    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '"+view+"'");
        }
        dispatcher.forward(request,response);
    }
}



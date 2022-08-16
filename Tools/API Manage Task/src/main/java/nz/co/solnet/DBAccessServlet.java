package nz.co.solnet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DBAccessServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(BootstrapConfig.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("taskid");
        String desc = request.getParameter("description");

        logger.info("Servlet doPost method triggered with ID: " + id);

        // validate input
        if (id != null) {
            // perform some functionality for testing purposes
            request.getSession().setAttribute("description", desc);
            response.sendRedirect("");

            /*
             * TODO add in DB queries that are triggered by the POST method
             */

        }
        else {
            request.setAttribute("error", "Unknown user, please try again");
            request.getRequestDispatcher("").forward(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        logger.info("Servlet doGet method triggered");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<br>Welcome to the servlet.<br>");


    }
}

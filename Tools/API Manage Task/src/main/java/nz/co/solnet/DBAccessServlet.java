package nz.co.solnet;

import nz.co.solnet.helper.DatabaseHelper;
import org.apache.derby.iapi.db.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;

public class DBAccessServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(BootstrapConfig.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
         * Currently there is limited functionality from the post parameters.
         *
         * I am using the different parameters as arguments by which to filter the action. This is not great for reliability
         * or very good practice in general. Once DB queries were implemented in a robust way I would minimize the parameters
         * being used and ensure that post commands where validated.
         *
         */
        boolean fetchAll = request.getParameter("fetchall") != null;
        boolean dropTable = request.getParameter("droptable") != null;
        boolean addRow = request.getParameter("add") != null;
        String id = request.getParameter("taskid");
        String desc = request.getParameter("description");

        logger.info("Servlet doPost method triggered with ID: " + id + " " + fetchAll + " " + dropTable + " " + addRow);

        // validate input
        if (fetchAll) {
            // perform some functionality for testing purposes
            request.getSession().setAttribute("description", desc);
            response.sendRedirect("");

            /*
             * TODO add in DB queries that are triggered by the POST method
             */
            DatabaseHelper.queryData();

        }
        // NOTE: this is a placeholder function used for testing
        else if(dropTable) {
            DatabaseHelper.cleanDatabase(); // drop
            DatabaseHelper.initialiseDB();  // re-initialise
            response.sendRedirect("");

        }
        // TODO expand the add row functionality to allow specification of parameters
        else if(addRow) {
            DatabaseHelper.insertData();
            response.sendRedirect("");
        }
        // handle error cases
        else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.print("<br>Sorry! This functionality has not been implemented yet!<br>");
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

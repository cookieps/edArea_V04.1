package controllers;

import com.avaje.ebean.Expr;
import models.Course;
import models.CourseContent;
import models.Notification;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by cookie on 14.11.14.
 */


@Security.Authenticated(Secured.class)
public class Content extends Controller {

    public static Result createContent(String id) {

        return ok(addContent.render(Form.form(CourseContent.class),id));
    }

    public static Result addContent(String id) {

        //Course currentCourse =  Course.find.where().like("email", "%"+request().username()+"%").like("courseName");
        //List<Course> course = Course.find.where().like("email", "%"+request().username()+"%").findList();

        //find.where().and(Expr.like("email_to", "%" + request().username() + "%"), Expr.like("email_from", "%"+email+"%")).findUnique().delete();



        System.out.println("__________________________________________");
        System.out.println(id);

        Form<CourseContent> createContentForm = Form.form(CourseContent.class).bindFromRequest();

        CourseContent newContent = new CourseContent(
                id,
                createContentForm.get().contentName,
                createContentForm.get().id,
                createContentForm.get().content,
                0,    // id content
                createContentForm.get().pathFile,
                request().username()
        );

        newContent.save();





        //List<Notification> notifications = new ArrayList<>();
        //notifications = Notification.find.where().like("email_to", "%"+request().username()+"%").findList();
        //return ok(index.render(User.find.byId(request().username()), notifications));
        return redirect(routes.Application.index());
    }



}

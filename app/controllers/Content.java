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

    public static Result createContent(String idCont) {
        try {                           //проверка принадлежит ли созданный курс данному пользователю
            Course cour = Course.find.where().like("courseName", "%" + idCont + "%").like("email", "%" + request().username() + "%").findUnique();

            if (cour.email.equals(request().username())) {

                List<CourseContent> contents = CourseContent.find.where().like("courseName", "%" + idCont + "%").findList();

                return ok(addContent.render(Form.form(CourseContent.class), idCont, contents));
            }
        }catch (NullPointerException e) {
           return redirect(routes.Application.index());
        }


        return redirect(routes.Application.index());
    }

    public static Result addContent(String idCont) {

        //Course currentCourse =  Course.find.where().like("email", "%"+request().username()+"%").like("courseName");
        //List<Course> course = Course.find.where().like("email", "%"+request().username()+"%").findList();



        //find.where().and(Expr.like("email_to", "%" + request().username() + "%"), Expr.like("email_from", "%"+email+"%")).findUnique().delete();

        if(CourseContent.find.where().like("id", "%" + null + "%").findUnique() == null) {

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }


        System.out.println("__________________________________________");
        System.out.println();

        Form<CourseContent> createContentForm = Form.form(CourseContent.class).bindFromRequest();

        CourseContent newContent = new CourseContent(
                idCont,                                                     // имя курса для создаваемого конента
                createContentForm.get().contentName,
                CourseContent.find.where().findRowCount()+1,            // уникальный айди
                createContentForm.get().content,
                createContentForm.get().contentId,    // id content                                     // айди для создания и группировки нескеольки конентов одного курса
                createContentForm.get().pathFile,
                request().username()                                    // создатель конента
        );

        newContent.save();





        //List<Notification> notifications = new ArrayList<>();
        //notifications = Notification.find.where().like("email_to", "%"+request().username()+"%").findList();
        //return ok(index.render(User.find.byId(request().username()), notifications));
        return redirect(routes.Application.index());
    }



    public static Result showContent()  {





        return  ok();
    }





}

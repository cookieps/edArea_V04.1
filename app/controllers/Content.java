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
           return ok(errorpage.render("Forbidden You don't have permission to access",403));
        }


        return ok(errorpage.render("Forbidden You don't have permission to access",403));


    }

    public static Result addContent(String idCont) {

        Form<CourseContent> createContentForm = Form.form(CourseContent.class).bindFromRequest();

        CourseContent newContent = new CourseContent(
                idCont,                                                    // имя курса для создаваемого конента
                createContentForm.get().contentName,
                CourseContent.find.where().findRowCount()+1,               // уникальный айди
                createContentForm.get().content,
                createContentForm.get().contentId,                         // айди для создания и группировки нескеольки конентов одного курса
                createContentForm.get().pathFile,
                request().username()                                       // создатель конента
        );

        newContent.save();



        return redirect(routes.Application.index());
    }



    public static Result showContent(String courseId, String contentId)  {

    List<CourseContent> content = CourseContent.find.where().like("courseName","%"+courseId+"%").like("contentName","%"+contentId+"%").findList();



        return ok(moreinfocontent.render(User.find.byId(request().username()),Course.find.byId(courseId),content));
    }





}

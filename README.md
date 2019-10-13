### Design & time note

Given a very short amount of time allocated for this task, a limited knowledge in the tools and technologies used I went for a solution that works but is *very* far from prod ready. I decided to submit as is and write a little explanation. The service works fine however the under-the-hood needs improvements.

### Stack & technology
I decided to use Scala, sbt, JSoup and Play. Play has a nice support for model-view-controller pattern but I have to admit I haven't worked much with Play before, so this was a novelty for me just like JSoup. 

### How application works
User sumbits a link which gets sanitized and passed to JSoup for extraction of HTML and then HTML inspection. There is a few main components:
- HomeController which serves the landing page
- HtmlInspectionController which delegates inspecting a POSTed link to appropiate service
- HtmlInspectionService which serves as a bridge between a controller and JSoup
- InspectorService trait which serves as a contract for a HTML inspector
- JSoupInspectorService which implements InspectorService and serves as a wrapper for JSoup. This is the only class where JSoup methods are called from.
- Util object
- Config object


### Error handling
The application has the basic error handling implemented: incorrect user input, timouts, problems or missing data related to JSoup.

### Challenge:
As I haven't worked closely with Play MVC or front-end before it was definitely the biggest challenge in the task given how little time I was allowed to take.

### Improvements to the application:
As I mentioned right in the beginning this solution is not near production ready. As much as I tried to keep it clean and ok-designed the time constraint took its toll on the way the project is designed, tested an structured.
- the code needs a general refactor. Right now the implementation is tied to JSoup which is called within functions which makes it impossible to pick and choose implementations and therefore properly test. I can imagine this code would be a perfect candidate to take advantage of higher order functions where I would be passing functions to functions rather than hardcoding implementation inside of the function.
- in addition to the above a large factor to why the application is not prod ready is the lack of proper testing. That's solely due to the hacky design. Should the above mentioned refactor happen, it would be much easier to pass mocked functions/objects and add proper unit and functional tests. Right now the tests are tied to JSoup implementation and they actually are calling real services which is unacceptable (unless we're speaking of acceptance test, but even then they wouldn't be carried out from within the application anyway). That also blocks me from testing the unhappy path. Testing is very important to me so this is a big no-go.
- would implement a less naive logic for login form search
- I would spend more time to implement a one page solution: call asynchronously using ajax and render the result under the text input. Right now the response comes back as a new page
- Use more of the type safe feature of Scala: I used a String and Int a number of times, it's good to alias them for added context
- Not use vars or mutable data structures
- Improve front end related code - it looks quite messy
- use a config lib like TypeSafe Config to work with application.conf instead of using an object
- have separate error handlers so that the code looks nice and clean, maybe use Cats for working with Try's and IO


### Running application
- `sbt run`
- wait for the server to start, you will see a log message "(Server started, use Enter to stop and go back to the console...)"
- go to the browser and open localhost:9000

### Running tests
`sbt test`

### Take away point:
The task was fun although I would have needed more time to deliver a good solution that would show off my real-life code standard. Even though the solution isn't perfect I do feel like I learnt something new, which is great :) This task was a nice reminder of how much more there is always to learn when it comes to programming and technology.

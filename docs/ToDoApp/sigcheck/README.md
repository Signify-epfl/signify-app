# Signature Checks

In order for your app to be compatible with our automated testing, we provide you with a signature file for some of the user stories that will ensure that you have the same API that we use. This file should be placed in a `sigchecks` folder the base of the kotlin code of your app (`app/src/main/java/com/github/se/bootcamp/sigchecks`).

In the real world, the vast majority of testing is automated, especially within the context of CI/CD pipelines. This ensures that code is consistently tested in a reliable and repeatable manner without human intervention (which would be very costly). This bootcamp reflects the industry standard by automatically testing your code. Signature checks are crucial to this process, as they align your code and API with our automated testing framework. By working within these constraints, you'll gain valuable experience in writing code that is compatible with automated test suites, an essential skill in modern software development.

> [!CAUTION]
> Please make sure that no error remains in those files at the end of each user story.

## Test tags

For testing and **grading**, we will use Espresso. We recommend that you use it for your own testing. To make our grading compatible, you will have to add test tags to some of the components in your UI, the lists of tags will be provided in each user story. See the [Figma](https://www.figma.com/design/IDm3NGS988Myo01P0Wa0Cr/TO-DO-APP-Mockup-FALL?node-id=435-3350&node-type=CANVAS&t=BZ8aut9M3fNlPJ8t-0) to make sure you didn't forget a test tag.

To do so, use the `testTag` modifier, for example:

```kt
Button(
  modifier = Modifier.testTag("greetingButton"),
  ...
)
```

> [!TIP]
> You can chain modifiers, e.g: `Modifier.padding(...).testTag(...)`

> [!NOTE]
> See the tutorial on [Android Testing](../../Tutorials/AndroidTesting.md) to understand how Espresso works.

> [Return to the Table of contents](../../../README.md)

# StockSim Development Team Workflow Overview

This documentation provides a standard workflow and documentation convention for
commiting changes on GitHub for **StockSim** development team.

In the workflow instruction, I'm showing Git commands on command line. Even
though you are free to use IDE tools if you prefer as long as following the same
workflow, I do suggest practice working with command line as this is what we
will be using in the industry (as works much faster once you get familiar).

## Branches

`main`: The deployment branch with the latest, fully implemented features. Code
on this branch should be stable and production-ready.

`dev`: The primary development branch where all feature branches merge once
completed. Periodically, changes from dev are merged into main to release a new
version.

## Start Working

Before start making changes to the codebase, create a new branch following these
steps:

1. Switch to main branch `git checkout main`.
2. Synchronize local branch with remote `git pull origin dev`.
3. Create a new branch for your work `git checkout -b <branch-name>`. If you are
   fixing a reported issue, name the branch `issue-<issue number from GitHub>`,
   otherwise pick a descriptive branch name that indicates the feature. (We are
   not naming the branch by username as in CSC207 labs; the best practice is to
   name branches by what they are doing).

## Commiting Work

After you make some changes to your local codebase, you can make a commit to
your local repository. The exact frequency of commiting the code is up to you,
but we do expect each commit to logically reflect one section of change. Below
is the workflow for commiting your work:

1. Make sure you are staying at the right branch `git branch`. If you are not on
   the right branch, run `git checkout <branch-name>`.
2. Check the unstaged file for commit `git status`.
3. Add the files you want to commit `git add <file1> <file2> ...`. It's OK to
   use `git add .` if you want to add all unstaged files to commit, but make
   sure not to include any irrelevant files.
4. Commit your change `git commit`. This should prompt you to write the git
   commit description in text editor (Vim by default), or you can directly add
   commit message in the command through
   `git commit -m "descriptions of your commit"`.

## Pushing Changes

Once you're done with the bug/feature you're working on, push this change to
your remote repository and make a pull request:

1. Double-check that you have committed all your changes `git status`.
2. Update the local repository with remote branch to incorporate other updates
   into your changes `git pull origin dev`. Resolve any conflicts if necessary.
3. Push your change to remote repository
   `git push --set-upstream origin <branch-name>`. The remote branch should have
   the same name as your local branch. Note that once you created the remote
   branch, you can simply write `git push` for further updates.

## Making and Reviewing Pull Request

After pushing changes to your remote repository, you can make a pull request.

1. Name your pull request following the naming convention of semantic
   versioning:
   1. `feat:` this change adds or modifies functionality. Example:
      `feat: implement buying stocks feature`
   2. `fix: ` this change fixes a bug in the codebase. Example:
      `fix: fix an error that leads to incorrect calculates of stock prices`
   3. `refactor: ` this change modifies the codebase without changing
      functionality. Example:
      `refactor: separate calculation of stock prices to a separate class`
   4. `docs: ` this change ONLY changes project documentation. Example:
      `docs: update README`
   5. `test: ` this change ONLY modifies tests. Example:
      `test: update unit tests for BuyStockInteractor`
   6. `ci: ` this change ONLY modifies CI/CD workflow. Example:
      `ci: set up automated CI testing for pull requests`
   7. `chores: ` trivial changes that do not fit into the above categories.
      Example: `chores: fix CheckStyle issues`
2. Summarize the proposed change in a few bullet points in the pull request
   message
3. Review and complete the checklist in the pull request template.
4. Request a review from another team member. The reviewer should ideally be the
   previous member working on the feature and should have a good understanding
   of the relevant part of codebase.
5. Once the reviewer receives review request, they should carefully review the
   submitted code according to the reviewer checklist and leave any comments as
   correction advice.

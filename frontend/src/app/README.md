# The `src/app` Directory

## Overview

```
src/
  |- app/
  |  |- home/
  |  |- about/
  |  |- app.js
  |  |- app.spec.js
```

### Testing

One of the design philosophies of `clouway-hr` is that tests should exist
alongside the code they test and that the build system should be smart enough to
know the difference and react accordingly. As such, the unit test for `app.js`
is `app.spec.js`, though it is quite minimal.

// Sample data for the view catalogue (doc 02 section 7). Pure data.
// This view has no props: it reads the countdown config the plugin puts on the page data ($page.data.countdownTimerConfig),
// so the catalogue shows it empty until that data is set.
export const notApplicable = ['empty', 'loading', 'error'];

/** @type {import('@panomc/plugin-kit').Samples} */
export default {
  filled: { props: {} },
};

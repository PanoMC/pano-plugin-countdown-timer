// Plugin-level options of the Pano plugin kit (@panomc/plugin-kit). The namespace is `countdown-timer` (the plugin id
// minus `pano-plugin-`); the views are the files of src/theme/views.
export default {
  styles: {
    // Every view is drawn from the admin's colours, background image, sizes and logo height, which only exist as values
    // at run time, so they stay inline style= exactly as before.
    styleAttrAllow: [
      'CountdownDisplay',
      'CountdownTimerCover',
      'CountdownTimerPage',
      'CountdownTimerSidebar',
      'OpeningEffect',
    ],
  },
};

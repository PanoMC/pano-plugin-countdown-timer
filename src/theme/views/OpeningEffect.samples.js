// Sample data for the view catalogue (doc 02 section 7). Pure data.
export const notApplicable = ['loading', 'error'];

/** @type {import('@panomc/plugin-kit').Samples} */
export default {
  filled: { props: { effectType: 'CONFETTI', active: true } },
  empty: { props: { effectType: 'NONE', active: false } },
  fireworks: { label: 'Fireworks', props: { effectType: 'FIREWORKS', active: true } },
  sparkle: { label: 'Sparkle', props: { effectType: 'SPARKLE', active: true } },
};

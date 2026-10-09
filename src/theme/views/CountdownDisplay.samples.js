// Sample data for the view catalogue (doc 02 section 7). Pure data.
export const notApplicable = ['empty', 'loading', 'error'];

const time = { days: 12, hours: 7, minutes: 45, seconds: 9 };
const labels = { daysLabel: 'Days', hoursLabel: 'Hours', minsLabel: 'Minutes', secsLabel: 'Seconds' };

/** @type {import('@panomc/plugin-kit').Samples} */
export default {
  filled: { props: { ...time, ...labels, style: 'SIMPLE', size: 'large', isDark: true } },
  boxed: { label: 'Boxed digits', props: { ...time, ...labels, style: 'BOXED', size: 'large', isDark: true } },
  circular: { label: 'Circular digits', props: { ...time, ...labels, style: 'CIRCULAR', size: 'large', isDark: false } },
  small: { label: 'Small (sidebar)', props: { ...time, ...labels, style: 'SIMPLE', size: 'small', isDark: true } },
};

{#if style === 'SIMPLE'}
  <!-- SIMPLE: Plain numbers with colon separators -->
  <div class="countdown-timer-countdown-display d-flex justify-content-center align-items-start gap-3 {cssClass}" style="{wrapperStyle}">
    <div class="text-center">
      <div class="{numClass}" style="{numStyle}">{pad(days)}</div>
      <small class="text-uppercase" style="opacity: 0.75; {labelFontStyle}">{daysLabel}</small>
    </div>
    <div class="{numClass}" style="opacity: 0.5; {numStyle}">:</div>
    <div class="text-center">
      <div class="{numClass}" style="{numStyle}">{pad(hours)}</div>
      <small class="text-uppercase" style="opacity: 0.75; {labelFontStyle}">{hoursLabel}</small>
    </div>
    <div class="{numClass}" style="opacity: 0.5; {numStyle}">:</div>
    <div class="text-center">
      <div class="{numClass}" style="{numStyle}">{pad(minutes)}</div>
      <small class="text-uppercase" style="opacity: 0.75; {labelFontStyle}">{minsLabel}</small>
    </div>
    <div class="{numClass}" style="opacity: 0.5; {numStyle}">:</div>
    <div class="text-center">
      <div class="{numClass}" style="{numStyle}">{pad(seconds)}</div>
      <small class="text-uppercase" style="opacity: 0.75; {labelFontStyle}">{secsLabel}</small>
    </div>
  </div>

{:else if style === 'BOXED'}
  <!-- BOXED: Numbers inside rounded boxes -->
  <div class="countdown-timer-countdown-display d-flex justify-content-center gap-2 gap-md-3 {cssClass}" style="{wrapperStyle}">
    <div class="text-center rounded-3 p-2 p-md-3" style="{boxBg} min-width: {boxMinWidth};">
      <div class="{numClass}" style="{numStyle}">{pad(days)}</div>
      <small class="text-uppercase d-block mt-1" style="opacity: 0.75; {labelFontStyle}">{daysLabel}</small>
    </div>
    <div class="text-center rounded-3 p-2 p-md-3" style="{boxBg} min-width: {boxMinWidth};">
      <div class="{numClass}" style="{numStyle}">{pad(hours)}</div>
      <small class="text-uppercase d-block mt-1" style="opacity: 0.75; {labelFontStyle}">{hoursLabel}</small>
    </div>
    <div class="text-center rounded-3 p-2 p-md-3" style="{boxBg} min-width: {boxMinWidth};">
      <div class="{numClass}" style="{numStyle}">{pad(minutes)}</div>
      <small class="text-uppercase d-block mt-1" style="opacity: 0.75; {labelFontStyle}">{minsLabel}</small>
    </div>
    <div class="text-center rounded-3 p-2 p-md-3" style="{boxBg} min-width: {boxMinWidth};">
      <div class="{numClass}" style="{numStyle}">{pad(seconds)}</div>
      <small class="text-uppercase d-block mt-1" style="opacity: 0.75; {labelFontStyle}">{secsLabel}</small>
    </div>
  </div>

{:else if style === 'CIRCULAR'}
  <!-- CIRCULAR: Numbers inside circles with ring borders -->
  <div class="countdown-timer-countdown-display d-flex justify-content-center gap-2 gap-md-3 {cssClass}" style="{wrapperStyle}">
    <div class="d-flex flex-column align-items-center">
      <div class="rounded-circle d-flex align-items-center justify-content-center" style="{circleBorder} width: {circleSize}; height: {circleSize};">
        <span class="{numClass}" style="{numStyle}">{pad(days)}</span>
      </div>
      <small class="text-uppercase mt-1" style="opacity: 0.75; {labelFontStyle}">{daysLabel}</small>
    </div>
    <div class="d-flex flex-column align-items-center">
      <div class="rounded-circle d-flex align-items-center justify-content-center" style="{circleBorder} width: {circleSize}; height: {circleSize};">
        <span class="{numClass}" style="{numStyle}">{pad(hours)}</span>
      </div>
      <small class="text-uppercase mt-1" style="opacity: 0.75; {labelFontStyle}">{hoursLabel}</small>
    </div>
    <div class="d-flex flex-column align-items-center">
      <div class="rounded-circle d-flex align-items-center justify-content-center" style="{circleBorder} width: {circleSize}; height: {circleSize};">
        <span class="{numClass}" style="{numStyle}">{pad(minutes)}</span>
      </div>
      <small class="text-uppercase mt-1" style="opacity: 0.75; {labelFontStyle}">{minsLabel}</small>
    </div>
    <div class="d-flex flex-column align-items-center">
      <div class="rounded-circle d-flex align-items-center justify-content-center" style="{circleBorder} width: {circleSize}; height: {circleSize};">
        <span class="{numClass}" style="{numStyle}">{pad(seconds)}</span>
      </div>
      <small class="text-uppercase mt-1" style="opacity: 0.75; {labelFontStyle}">{secsLabel}</small>
    </div>
  </div>

{:else if style === 'MINIMAL'}
  <!-- MINIMAL: Just large numbers inline, no labels -->
  <div class="countdown-timer-countdown-display d-flex justify-content-center align-items-center gap-2 {cssClass}" style="font-family: 'Courier New', monospace; {wrapperStyle}">
    <span class="{numClass}" style="{numStyle}">{pad(days)}</span>
    <span class="{numClass}" style="opacity: 0.4; {numStyle}">:</span>
    <span class="{numClass}" style="{numStyle}">{pad(hours)}</span>
    <span class="{numClass}" style="opacity: 0.4; {numStyle}">:</span>
    <span class="{numClass}" style="{numStyle}">{pad(minutes)}</span>
    <span class="{numClass}" style="opacity: 0.4; {numStyle}">:</span>
    <span class="{numClass}" style="{numStyle}">{pad(seconds)}</span>
  </div>

{:else if style === 'SEPARATED'}
  <!-- SEPARATED: Each digit is a separate box (airport/train station style) -->
  <div class="countdown-timer-countdown-display d-flex justify-content-center gap-1 gap-md-2 flex-wrap {cssClass}" style="{wrapperStyle}">
    {#each digitGroups as group, gi}
      {#if gi > 0}
        <div class="d-flex align-items-center px-1">
          <span class="{numClass}" style="opacity: 0.4; {numStyle}">:</span>
        </div>
      {/if}
      <div class="d-flex flex-column align-items-center">
        <div class="d-flex gap-1">
          {#each group.digits as digit}
            <div class="rounded-2 d-flex align-items-center justify-content-center"
              style="{boxBg} width: {digitBoxSize}; height: {digitBoxHeight};">
              <span class="{numClass}" style="{numStyle}">{digit}</span>
            </div>
          {/each}
        </div>
        <small class="text-uppercase mt-1" style="opacity: 0.75; {labelFontStyle}">{group.label}</small>
      </div>
    {/each}
  </div>
{/if}

<script>
  let {
    days = 0,
    hours = 0,
    minutes = 0,
    seconds = 0,
    style = 'SIMPLE',
    size = 'large',
    daysLabel = 'Days',
    hoursLabel = 'Hours',
    minsLabel = 'Minutes',
    secsLabel = 'Seconds',
    isDark = true,
  } = $props();

  function pad(n) {
    return String(Math.max(0, n)).padStart(2, '0');
  }

  // Size-dependent classes and styles
  let numClass = $derived(size === 'small' ? 'fw-bold fs-5' : 'fw-bold');
  let numStyle = $derived(size === 'small' ? '' : 'font-size: 5rem; line-height: 1;');
  let labelFontStyle = $derived(size === 'small' ? 'font-size: 0.7rem;' : 'font-size: 0.9rem; letter-spacing: 2px;');
  let cssClass = $derived(size === 'small' ? 'mb-2' : 'mb-4');
  let wrapperStyle = $derived('');

  // Box style for BOXED and SEPARATED
  let boxBg = $derived(isDark
    ? 'background: rgba(255,255,255,0.1);'
    : 'background: rgba(0,0,0,0.06);');

  let boxMinWidth = $derived(size === 'small' ? '50px' : '120px');

  // Circle style
  let circleSize = $derived(size === 'small' ? '52px' : '110px');
  let circleBorder = $derived(isDark
    ? 'border: 3px solid rgba(255,255,255,0.3); background: rgba(255,255,255,0.05);'
    : 'border: 3px solid rgba(0,0,0,0.15); background: rgba(0,0,0,0.03);');

  // Separated digit boxes
  let digitBoxSize = $derived(size === 'small' ? '28px' : '60px');
  let digitBoxHeight = $derived(size === 'small' ? '38px' : '80px');

  let digitGroups = $derived([
    { digits: pad(days).split(''), label: daysLabel },
    { digits: pad(hours).split(''), label: hoursLabel },
    { digits: pad(minutes).split(''), label: minsLabel },
    { digits: pad(seconds).split(''), label: secsLabel },
  ]);
</script>
